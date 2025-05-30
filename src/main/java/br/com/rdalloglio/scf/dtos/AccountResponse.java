package br.com.rdalloglio.scf.dtos;

import java.math.BigDecimal;

public record AccountResponse(Long id, String name, BigDecimal balance) { }
