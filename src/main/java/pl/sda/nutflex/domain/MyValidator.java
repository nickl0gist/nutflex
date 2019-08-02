package pl.sda.nutflex.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MyValidator implements ConstraintValidator<ReleaseDateValidation, Movie> {
    @Override
    public void initialize(ReleaseDateValidation releaseDateValidation) {

    }

    @Override
    public boolean isValid(Movie movie, ConstraintValidatorContext constraintValidatorContext) {
        return movie.getReleaseDate().isAfter(LocalDate.of(1996,12,20));
    }
}
