package com.websocket.board.controller;

import com.websocket.board.config.JwtTokenProvider;
import com.websocket.board.model.Channel;
import com.websocket.board.model.LoginInfo;
import com.websocket.board.repo.ChannelRedisRepository;
import com.websocket.board.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class ChannelController {

    private final ChannelRedisRepository channelRedisRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ChannelService channelService;

    @GetMapping("/channels")
    @ResponseBody
    public List<Channel> channel() {
        List<Channel> channels = channelRedisRepository.findAllChannel();
        channels.stream().forEach(channel -> channel.setUserCount(channelRedisRepository.getUserCount(channel.getChannelId())));
        return channels;
    }

    @PostMapping("/channel")
    @ResponseBody
    public Channel createChannel(@RequestParam("channelName") String channelName) {
        String channelId = UUID.randomUUID().toString();
        // save channel in redis
        Channel channel = channelRedisRepository.createChannel(channelName, channelId);
        // save channel in mariadb
        channelService.saveChannel(channelName, channelId);
        return channel;
    }

    @GetMapping("/channel/{channelId}")
    @ResponseBody
    public Channel channelInfo(@PathVariable String channelId) {
        return channelRedisRepository.findChannelById(channelId);
    }

    @GetMapping("/user")
    @ResponseBody
    public LoginInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return LoginInfo.builder()
                .name(name)
                .token(jwtTokenProvider.generateToken(name))
                .build();
    }
}