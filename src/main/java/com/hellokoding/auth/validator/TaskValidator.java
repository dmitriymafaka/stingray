package com.hellokoding.auth.validator;

import com.hellokoding.auth.model.Task;
import com.hellokoding.auth.model.User;
import com.hellokoding.auth.service.DateService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.Date;

@Component
public class TaskValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Task task = (Task) o;

        if (task.getDescription().length() > 100) {
            errors.rejectValue("description", "Long.task.description");
        }

        try {
            Date deadline = DateService.convertToCalendar(task.getDate()).getTime();
            Date now = Calendar.getInstance().getTime();
            if (deadline.before(now)) {
                errors.rejectValue("date", "Passed.task.deadline");
            }
            if ((deadline.getTime()-now.getTime())/1000/60/60/24 > 180) {
                errors.rejectValue("date", "Far.task.deadline");
            }
            if (task.getDeadline() != null && task.getDeadline().getTime().before(deadline)){
                errors.rejectValue("date", "Extend.task.deadline");
            }
            task.setDeadline();
        } catch (Exception e) {
            errors.rejectValue("date", "Invalid.task.deadline");
        }
    }

    public void validateCandidate(Task task, User candidate, Errors errors){
        if(!task.getCandidates().contains(candidate))
            errors.rejectValue("candidates", "No.task.candidate");
    }
}