package com.github.fabriciolfj.redissontest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {

    private String name;
    private String city;
    private List<Integer> marks;
}
