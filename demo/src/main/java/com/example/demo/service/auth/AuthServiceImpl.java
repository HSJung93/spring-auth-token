package com.example.demo.service.auth;

import com.example.demo.domain.UserRole;
import com.example.demo.domain.member.Member;
import com.example.demo.domain.member.MemberRepository;
import com.example.demo.domain.salt.SaltRepository;
import com.example.demo.domain.salt.Salt;
import com.example.demo.service.mail.EmailService;
import com.example.demo.service.salt.SaltUtil;
import com.example.demo.service.token.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import javassist.NotFoundException;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private MemberRepository memberRepository;

//    @Autowired
//    private SaltRepository saltRepository;

    @Autowired
    private SaltUtil saltUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailService emailService;

    @Override
    public void signUpUser(Member member){
        String password = member.getPassword();
        String salt = saltUtil.genSalt();
        log.info(salt);
        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt, password));
        System.out.println(member);
        memberRepository.save(member);
    }

    @Override
    public Member loginUser(String id, String password) throws Exception{

        Member member = memberRepository.findByUsername(id);

        if(member == null) throw new Exception("멤버가 조회되지 않습니다.");
        String salt = member.getSalt().getSalt();

        password = saltUtil.encodePassword(salt, password);
        if(! member.getPassword().equals(password)) throw new Exception("비밀번호가 틀립니다");

        return member;
    }

    @Override
    public Member findByUsername(String username) throws NotFoundException {
        Member member = memberRepository.findByUsername(username);
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음");
        return member;
    }

    @Override
    public void sendVerificationMail(Member member) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/user/verify/";
        if(member==null) throw new NotFoundException("멤버가 조회되지 않습니다.");
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(),member.getUsername(), 60 * 30L);
        emailService.sendMail(member.getEmail(),"[스토브 개인 과제] 회원가입 인증메일입니다.",VERIFICATION_LINK+uuid.toString());
    }

    @Override
    public void verifyEmail(String key) throws NotFoundException {
        String memberId = redisUtil.getData(key);
        Member member = memberRepository.findByUsername(memberId);
        if(member==null) throw new NotFoundException("멤버가 조회되지 않습니다.");
        modifyUserRole(member, UserRole.ROLE_USER);
        redisUtil.deleteData(key);
    }

    @Override
    public void modifyUserRole(Member member, UserRole userRole){
        member.setRole(userRole);
        memberRepository.save(member);
    }

    @Override
    public boolean isPasswordUuidValidate(String key){
        String memberId = redisUtil.getData(key);
        return !memberId.equals("");
    }

    @Override
    public void changePassword(Member member,String password) throws NotFoundException{
        if(member == null) throw new NotFoundException("changePassword(),멤버가 조회되지 않음");
        String salt = saltUtil.genSalt();
        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt,password));
        memberRepository.save(member);
    }

    @Override
    public void requestChangePassword(Member member) throws NotFoundException{
        String CHANGE_PASSWORD_LINK = "http://localhost:8080/user/password/";
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음.");
        String key = REDIS_CHANGE_PASSWORD_PREFIX+UUID.randomUUID();
        redisUtil.setDataExpire(key,member.getUsername(),60 * 30L);
        emailService.sendMail(member.getEmail(),"[스토브 개인과제] 사용자 비밀번호 안내 메일",CHANGE_PASSWORD_LINK+key);
    }


}
