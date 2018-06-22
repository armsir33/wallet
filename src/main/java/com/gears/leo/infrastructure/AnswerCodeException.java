package com.gears.leo.infrastructure;

import org.apache.commons.lang3.Validate;

import com.gears.leo.domain.AnswerCode;

public class AnswerCodeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final AnswerCode answerCode;

    public AnswerCodeException(final AnswerCode answerCode) {
        this.answerCode = Validate.notNull(answerCode, "Required, valid answer code");
    }

    @Override
    public String getMessage() {
        return this.answerCode.getDescription();
    }
}
