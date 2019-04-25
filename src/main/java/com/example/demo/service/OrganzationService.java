package com.example.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * feign接口（接口实现类实际已经不会走了）
 *
 * @author 张丰
 * @version v1.0
 */
@SuppressWarnings("ALL")
@FeignClient(name = "ORGANIZATIONSERVICE",fallback = HystrixClientFallback.class)
@Service
public interface OrganzationService {
    @RequestMapping(method = RequestMethod.GET, value = "/v1/organizations/1/licenses/{name}",
            consumes = "application/json")
    public String sayHello(@PathVariable("name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/organizations/1/licenses/{name}",
            consumes = "application/json")
    public String saySorry(@PathVariable("name") String name);
}
