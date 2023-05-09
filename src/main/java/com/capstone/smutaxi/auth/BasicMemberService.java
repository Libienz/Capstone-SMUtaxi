package com.capstone.smutaxi.auth;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasicMemberService implements MemberService{

    private final MemberRepository memberRepository = new MemoryMemberRepoisitory();

    /**
     * 회원가입
     * @param member
     */
    @Override
    public void join(Member member) {
        //같은 id 안됨
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findById(member.getId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                });
    }

    /**
     * 전체 회원 조회
     *
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(String memberId) {
        return memberRepository.findById(memberId);
    }


    @Override
    public boolean login(String id, String pw) {
        Optional<Member> member = memberRepository.findById(id);
        if (member == null){ //아이디 존재하지 않음
            return false;
        } else{
            if (pw.equals(member.get().getPassword())) {
                return true;
            } else {
                return false; //비밀번호 틀림
            }
        }

    }
}
