package net.tiny.penguin.calendar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/meeting")
public class MeetingController {

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String add(@RequestParam(value = "day") String day) {
        return "Owkey";
    }
}
