package gae.piaz.fbsec.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.security.Principal;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final FirebaseAuth firebaseAuth;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "/test-auth")
    public String getPrincipalName(Principal principal) {
        return principal.getName();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "/user-claims/{uid}")
    public void addAuthority(@PathVariable String uid, @RequestBody String authorityToAdd)
            throws FirebaseAuthException {

        Map<String, Object> currentClaims = firebaseAuth.getUser(uid).getCustomClaims();

        ArrayList<String> rolesOld =
                (ArrayList<String>) currentClaims.getOrDefault("authorities", List.of());
        Set<String> rolesNew = new HashSet<>(rolesOld);
        rolesNew.add(authorityToAdd);

        HashMap<String, Object> newClaims = new HashMap<>(currentClaims);
        newClaims.put("authorities", rolesNew);
        firebaseAuth.setCustomUserClaims(uid, newClaims);
    }

}