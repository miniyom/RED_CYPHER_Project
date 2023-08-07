package com.cyphers.game.RecordSearch.service.user;

import com.cyphers.game.RecordSearch.model.CrsUser;
import com.cyphers.game.RecordSearch.service.user.repository.CrsUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CrsUserService {

    private final CrsUserRepository crsUserRepository;

    // 회원가입
    public void register(String userId, String nickname) {
        upsertUser(CrsUser.builder()
                .userId(userId)
                .nickname(nickname)
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .build());
    }

    // 회원정보 변경
    public void upsertUser(CrsUser user) {
        user.setModDate(LocalDateTime.now());
        crsUserRepository.save(user);
    }

    public void delete(CrsUser user) {
        crsUserRepository.delete(user);
    }

    public List<CrsUser> getUserList() {
        return crsUserRepository.findAll();
    }

    public Optional<CrsUser> getUser(String userId) {
        return crsUserRepository.findById(userId);
    }
}
