package org.coding.webclient.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
    private Integer id;
    private Integer userId;
    private String title;
    private String body;
}
