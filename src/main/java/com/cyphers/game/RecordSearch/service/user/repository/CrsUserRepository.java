package com.cyphers.game.RecordSearch.service.user.repository;

import com.cyphers.game.RecordSearch.model.CrsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CrsUserRepository extends JpaRepository<CrsUser, String> {

    List<CrsUser> findByNicknameOrderByModDate(String nickname);
}
