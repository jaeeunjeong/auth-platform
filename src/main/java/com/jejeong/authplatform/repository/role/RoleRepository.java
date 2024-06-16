package com.jejeong.authplatform.repository.role;

import com.jejeong.authplatform.entity.member.Role;
import com.jejeong.authplatform.entity.member.RoleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRoleType(RoleType roleType);
}
