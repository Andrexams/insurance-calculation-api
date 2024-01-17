package br.com.martins.insurancecalculationapi.common.adapter.rest;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CustomErrorResponse {


    private String type;
    private List<String> message;
    private LocalDateTime timestamp;
}
