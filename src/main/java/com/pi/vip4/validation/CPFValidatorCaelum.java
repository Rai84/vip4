package com.pi.vip4.validation;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CPFValidatorCaelum implements ConstraintValidator<CPFValid, String> {

    @Override
    public void initialize(CPFValid constraintAnnotation) {
        // Inicialização (não é necessária neste caso)
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isEmpty()) {
            return false; // CPF não pode ser nulo ou vazio
        }
        try {
            CPFValidator validator = new CPFValidator();
            validator.assertValid(cpf); // Validação do CPF usando Caelum Stella
            return true;
        } catch (InvalidStateException e) {
            return false; // CPF inválido
        }
    }
}
