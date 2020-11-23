package com.websocket.board.model.crud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CRUDModule implements Serializable {
    private CRUDType crudType;
    private ModuleType moduleType;
    private Object moduleObject;
}
