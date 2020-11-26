package com.example.springboot.web.springbootfirstwebapplication.controller;

import com.example.springboot.web.springbootfirstwebapplication.model.Todo;
import com.example.springboot.web.springbootfirstwebapplication.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;
import java.util.Date;


@Controller
@SessionAttributes("name")
public class TodoController {

    @Autowired
    TodoService service;

    @RequestMapping(value="/list-todos", method = RequestMethod.GET)
    public String showTodos(ModelMap model){
        String name = getLoggedUserName(model);
        model.put("todos", service.retrieveTodos(name));
        return "list-todos";
    }

    private String getLoggedUserName(ModelMap model) {
        return (String) model.get("name");
    }

    @RequestMapping(value="/add-todo", method = RequestMethod.GET)
    public String showAddTodoPage(ModelMap model){
        model.addAttribute("todo", new Todo(0, getLoggedUserName(model),
                "", new Date(), false));
        return "add-todo";
        }

    @RequestMapping(value="/add-todo", method = RequestMethod.POST)
    public String addTodo(ModelMap model, @Valid Todo todo, BindingResult result){
        if (result.hasErrors()){
            return "add-todo";
        }
        service.addTodo(getLoggedUserName(model), todo.getDesc(), todo.getTargetDate(), false);
        return "redirect:/list-todos";
    }

    @RequestMapping(value="/delete-todo", method = RequestMethod.GET)
    public String deleteTodo(ModelMap model,@RequestParam int id){
        service.deleteTodo(id);
        return "redirect:/list-todos";
    }

    @RequestMapping(value = "/update-todo", method = RequestMethod.GET)
    public String showUpdateTodoPage(@RequestParam int id, ModelMap model) {
        Todo todo = service.retrieveTodo(id);
        model.put("todo", todo);
        return "add-todo";
    }

    @RequestMapping(value = "/update-todo", method = RequestMethod.POST)
    public String updateTodo(ModelMap model, @Valid Todo todo,
                             BindingResult result) {

        if (result.hasErrors()) {
            return "add-todo";
        }

        todo.setUser(getLoggedUserName(model));

        service.updateTodo(todo);

        return "redirect:/list-todos";
    }
}