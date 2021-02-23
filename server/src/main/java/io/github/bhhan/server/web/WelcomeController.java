package io.github.bhhan.server.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

@RestController
@RequestMapping("/resource/language")
public class WelcomeController {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getFruits(){
        return "jp";
    }
}
