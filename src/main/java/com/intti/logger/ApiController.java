/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.intti.logger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author Lenovo
 */
@Controller
public class ApiController {
    @Autowired
    private EntryRepository EntityRepository;
    
    @GetMapping("/health")
    @ResponseBody
    public String getTest(){
        return "OK";
    }
    @DeleteMapping("/entries/{id}")
    @ResponseBody
    public String deleteEntityById(@PathVariable Long id){
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        this.EntityRepository.deleteAllById(ids);
        return("done");
    }
    @GetMapping("/entries")
    @ResponseBody
    public List<Entry> getEntries(){
        
        return this.EntityRepository.findAll(Sort.by(Sort.Direction.DESC, "time"));
    }
    @GetMapping("/entries/{id}")
    @ResponseBody
    public Entry getEntry(@PathVariable Long id){
        return this.EntityRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found"));
    }
    @PostMapping("/entries")
    public String postEntry(
            @RequestParam String title,
            @RequestParam String body,
            @RequestParam(required = false ) String time,
            @RequestParam(required = false ) String latitude,
            @RequestParam(required = false ) String longitude,
            @RequestParam(value = "istime", required = false, defaultValue = "false") boolean istime){
        Entry newEntry = new Entry();
        newEntry.setTitle(title);
        newEntry.setBody(body);
        if(time==null){
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedNow = now.format(formatter);
            newEntry.setTime(formattedNow);
        }else{
            newEntry.setTime(time);
        }
        

        try{
            newEntry.setLatitude(Double.parseDouble((String) latitude));
        }catch  (NumberFormatException e) {
            newEntry.setLatitude(null);
        }
        try{
            newEntry.setLongitude(Double.parseDouble((String) longitude));
        }catch  (NumberFormatException e) {
           newEntry.setLongitude(null);
 
        }
        System.out.println(title);
        this.EntityRepository.save(newEntry);
        return("index");
    }
}
