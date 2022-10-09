package fcode.backend.management;

import fcode.backend.management.config.Role;
import fcode.backend.management.model.dto.MemberDTO;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.PositionRepository;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.MemberService;
import fcode.backend.management.service.constant.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PositionRepository positionRepository;

    @Test
    void tesst() {
        Member member = memberRepository.findMemberById(1);

    }

//    @Test
//    void getAllMember() {
//        List<Member> memberDTOList = positionRepository.getMembersByPositionId(1);
//        memberDTOList.forEach(System.out::println);
//    }

//    @Test
//    void createMember() {
//        memberService.register(new MemberDTO("Huỳnh", "Phước", "h1", "SE", null, "Student", null, "071234122", "h12@gmail.com", "h12@gmail.com", "tes12", "tes1"));
//        memberService.register(new MemberDTO("Huỳnh", "Phước", "h1", "SE", null, "Student", null, "0743224", "h22@gmail.com", "h22@gmail.com", "tes22", "tes1"));
//        memberService.createMember(new MemberDTO("Huỳnh", "Nguyên", "h1", "SE", null, "Student", null, "07825345", "h32@gmail.com", "h32@gmail.com", "tes32", "tes1"));
//    }
//
//    @Test
//    void updateMember() {
//        memberService.updateMember(new MemberDTO(1, "SE160419", "Tấn", "Phước", null, "SE", new Date(), Role.MEMBER, new Date(), "01234455", null, null, "https", "192.168.1.1"));
//    }
//
//    @Test
//    void deleteMember() {
//        memberService.deleteMember(1);
//    }
}
