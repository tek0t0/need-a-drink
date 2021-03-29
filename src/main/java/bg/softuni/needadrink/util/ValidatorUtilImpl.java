package bg.softuni.needadrink.util;


import javax.validation.Validator;

public class ValidatorUtilImpl implements ValidatorUtil {
    private final Validator validator;

    public ValidatorUtilImpl(Validator validator) {
        this.validator = validator;
    }


    @Override
    public <E> boolean isValid(E entity) {
       return this.validator.validate(entity).isEmpty();
    }



}
