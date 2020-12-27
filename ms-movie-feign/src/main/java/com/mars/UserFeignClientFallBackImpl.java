package com.mars;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserFeignClientFallBackImpl implements UserFeignClient{
    @Setter
    private Throwable cause;

    @Override
    public User findById(Long id) {
        log.error("获取用户失败, " + cause.getMessage());
        return new User(0, "defaultUser");
    }
}
