package com.example.backend.Service;

import com.example.backend.Model.Parameter;
import com.example.backend.Repository.ParameterRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParameterService {
    private final ParameterRepository repository;

    public ParameterService(ParameterRepository repository) {
        this.repository = repository;
    }

    public List<Parameter> getAll() {
        return repository.findAll();
    }

    public Parameter updateParam(Parameter p) {
        return repository.save(p);
    }

    public String getValueByKey(String key) {
        return repository.findById(key)
                .map(Parameter::getValueParam)
                .orElse(null);
    }

    // ✅ الميثود الجديدة اللي يستحقها الـ Config باش يتثبت بالـ Code والـ Type
    public Parameter getByCodeAndType(String code, String type) {
        return repository.findByCodeParametreAndTypeParametre(code, type).orElse(null);
    }

    // ميثود مساعدة لجلب الكائن كامل بالـ Key
    public Parameter getParameterById(String key) {
        return repository.findById(key).orElse(null);
    }
}