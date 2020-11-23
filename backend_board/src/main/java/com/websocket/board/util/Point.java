package com.websocket.board.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    int top_left_x;
    int top_left_y;
    int size_width;
    int size_height;
    int rotate_angle;

}
