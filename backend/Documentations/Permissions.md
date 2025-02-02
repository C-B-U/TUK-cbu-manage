## Permission
`Permission`은 URI 단위로 적용 가능한 멤버 권한이다.
`<package>.authentication.authorization.Permission` Enum을 수정하면 추가할 수 있다.
해당 정보는 `login_entity` 테이블에 저장된다.

## Role
`Role`는 멤버 권한으로 주로 Controller에서 AccessToken의 권한을 확인해 접근을 제한하는 역할로 사용된다.
`cbu_member` 테이블에 저장되며 실제로 저장될 때는 bit로 저장된다.
`<package>.model.enums.Role` Enum 선언해 추가할 수 있으며 `Role`의 `value`는 저장될 bit 자리를 의미한다.

