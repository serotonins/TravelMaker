package com.ssafy.gumibom.global.common;

import jakarta.persistence.*;
import lombok.Getter;

@Embeddable
@Getter
public class Category {
    private String name;
    private String color;
}
