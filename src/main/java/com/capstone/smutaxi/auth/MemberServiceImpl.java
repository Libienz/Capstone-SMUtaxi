package com.capstone.smutaxi.auth;

import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{

    private MemberRepository memberRepository;

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(String id) {
        return memberRepository.findById(id);
    }

    @Override
    public boolean login(String id, String pw) {
        Member member = findMember(id);
        if (member == null){ //아이디 존재하지 않음
            return false;
        } else{
            if (pw.equals(member.getPassword())) {
                return true;
            } else {
                return false; //비밀번호 틀림
            }
        }

    }
}
