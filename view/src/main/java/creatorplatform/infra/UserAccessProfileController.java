package creatorplatform.infra;

import creatorplatform.domain.*;
import creatorplatform.service.UserAccessProfileService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/view")
@Transactional
public class UserAccessProfileController {

    @Autowired
    private UserAccessProfileService userAccessProfileService;

    // 기존 메소드 주석처리 (필요시 나중에 삭제)
    /*
    @RequestMapping(
        value = "/userAccessProfiles/{id}/accesstocontent",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public UserAccessProfile accessToContent(
        @PathVariable(value = "id") Long id,
        @RequestBody AccessToContentCommand accessToContentCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /userAccessProfile/accessToContent  called #####"
        );
        
        return userAccessProfileService.accessToContent(id, accessToContentCommand);
    }
    */

    // 책 읽기 접근 권한 확인
    @GetMapping("/{id}/accesstocontent")
    public ResponseEntity<Map<String, Object>> accessToContent(
        @PathVariable Long id,
        @RequestParam Long productId
    ) {
        try {
            Map<String, Object> result = userAccessProfileService.accessToContent(id, productId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("hasAccess", false);
            error.put("reason", "ERROR");
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    // 포인트 구매 가능 여부 확인
    @GetMapping("/{id}/checkpurchaseability")
    public ResponseEntity<Map<String, Object>> checkPurchaseability(
        @PathVariable Long id,
        @RequestParam Long productId
    ) {
        try {
            Map<String, Object> result = userAccessProfileService.checkPurchaseability(id, productId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("canPurchase", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    // 포인트로 구매 실행
    @PostMapping("/{id}/purchasewithpoints")
    public ResponseEntity<Map<String, Object>> purchaseWithPoints(
        @PathVariable Long id,
        @RequestParam Long productId
    ) {
        try {
            Map<String, Object> result = userAccessProfileService.purchaseWithPoints(id, productId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
