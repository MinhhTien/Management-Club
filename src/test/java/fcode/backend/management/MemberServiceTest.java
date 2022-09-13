package fcode.backend.management;

import fcode.backend.management.config.Role;
import fcode.backend.management.model.dto.MemberDTO;
import fcode.backend.management.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

//    @Test
//    void getAllMember() {
//        List<MemberDTO> memberDTOList = memberService.getAllMembers().getData();
//        memberDTOList.forEach(System.out::println);
//    }
//
//    @Test
//    void createMember() {
//        memberService.createMember(new MemberDTO("SE160414", "Huỳnh", "Phước", "h1", "SE", null, "Student", null, "071234122", "h12@gmail.com", "h12@gmail.com", "tes12", "tes1"));
//        memberService.createMember(new MemberDTO("SE160411", "Huỳnh", "Phước", "h1", "SE", null, "Student", null, "0743224", "h22@gmail.com", "h22@gmail.com", "tes22", "tes1"));
//        memberService.createMember(new MemberDTO("SE160321", "Huỳnh", "Nguyên", "h1", "SE", null, "Student", null, "07825345", "h32@gmail.com", "h32@gmail.com", "tes32", "tes1"));
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
