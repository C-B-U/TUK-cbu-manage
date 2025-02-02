## 설정법
`Permission` Enum에 설정된 URI 범위 내에 있는 Controller에서 Parameter에 `AccessToken`을 설정하면 Token 값이 주입된다.

```java
@RestController
public class TestController {
	@GetMapping
    public void test(AccessToken accessToken) {
		if (!accessToken.getRole().contains(Role.ADMIN)) {
			throw new AuthenticationException("You don't have permission");
		}
		// do something
    }
}
```