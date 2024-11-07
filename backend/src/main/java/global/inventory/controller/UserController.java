package global.inventory.controller;

import global.inventory.mapper.UserMapper;
import global.inventory.payload.request.UserDetailsResponse;
import global.inventory.payload.response.generic.InventoryResponse;
import global.inventory.payload.response.generic.PageInfo;
import global.inventory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public InventoryResponse<List<UserDetailsResponse>> getAllNonAdminUsers(
            @PageableDefault Pageable pageable) {

        var data = userService.getAllNonAdminUsers(pageable);

        return new InventoryResponse<>(
                UserMapper.INSTANCE.userListToDtoList(data.getContent()),
                PageInfo.of(data)
        );
    }

}
