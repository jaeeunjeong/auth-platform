# 엔티티 구조

### User
| 필드명        | 타입           | 설명       |
|------------| -------------- |----------|
| id         | Long           | 사용자 ID   |
| email      | String         | 이메일 주소   |
| username   | String         | 사용자 이름   |
| nickname   | String         | 사용자 닉네임  |
| password   | String         | 비밀번호     |
| roles     | String         | 회원 등급 정보 |
| created_at | LocalDateTime  | 생성 일시    |
| updated_at | LocalDateTime  | 수정 일시    |

### Role
| 필드명     | 타입           | 설명    |
| ---------- | -------------- |-------|
| id         | Long           | 역할 ID |
| roleType       | String         | 역할 종류 |

### MemberRole
| 필드명     | 타입           | 설명            |
| ---------- | -------------- | --------------- |
| user_id    | Long           | 사용자 ID       |
| role_id    | Long           | 역할 ID         |

### MemberRoleId
| 필드명     | 타입           | 설명            |
| ---------- | -------------- | --------------- |
| user_id    | Long           | 사용자 ID       |
| role_id    | Long           | 역할 ID         |