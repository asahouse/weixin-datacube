package com.andc.amway.datacubecatcher.service;

import com.andc.amway.datacubecatcher.utils.KCRandomCharData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserManager {

    private static String flag_user_token = "datacube:user:token:";
    private static int user_token_expire_time= 600;

    @Value("${datacubeUsers}")
    private String datacubeUsers;

    @Autowired
    JedisPool jedisPool;

    public boolean haslogin(String token){
        if (StringUtils.isEmpty(token)) return false;

        try(Jedis jedis = jedisPool.getResource()){
            return jedis.exists(flag_user_token + token);
        }
    }

    public Optional<String> login(String name, String password){
        if (StringUtils.isEmpty(datacubeUsers))return Optional.empty();

        List<String[]> users = Arrays.stream(datacubeUsers
                .split(",")).map(kv -> kv.split(":"))
                .collect(Collectors.toList());

        Optional<String[]> isFind = users.parallelStream()
                .filter(user -> user[0].equals(name) && user[1].equals(password))
                .findAny();

        if (isFind.isPresent()){

            String token = KCRandomCharData.createRandomCharData(12);
            try(Jedis jedis = jedisPool.getResource()){
                if (StringUtils.isEmpty(token)) return Optional.empty();

                jedis.set(flag_user_token + token,String.valueOf(System.currentTimeMillis()));
                jedis.expire(token, user_token_expire_time);
                return Optional.of(token);
            }
        }else return Optional.empty();
    }

    public void logout(String token){
        if (StringUtils.isEmpty(token)) return;

        try(Jedis jedis = jedisPool.getResource()){
            if (jedis.exists(flag_user_token + token)) jedis.del(token);
        }
    }
}
