package com.autodoc.ai.promptmanager.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ParallelPromptChainImpl implements ParallelPromptChain{

    private List<PromptChain> promptChains;

    public ParallelPromptChainImpl(PromptChain... promptChains) {
        this.promptChains = List.of(promptChains);
    }

    public ParallelPromptChainResult execute(Map<String, String> variables) {
        try(var taskExecutor = Executors.newVirtualThreadPerTaskExecutor()){
            var tasks = this.promptChains.stream()
                    .map(p -> (Callable<Object>) () -> p.execute(variables)).toList();

            List<Future<Object>> futures;
            try {
                futures = taskExecutor.invokeAll(tasks);
            } catch (InterruptedException e) {
                throw new RuntimeException("Erro ao processar taskExecutor", e);
            }

            List<Object> results = futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException("Erro durante a execução da task", e);
                        }
                    })
                    .toList();

            Map<Class, Object> response = new HashMap<>();
            results.forEach(r -> response.put(r.getClass(), r));
            return new ParallelPromptChainResult(response);
        }
    }

//    public ParallelPromptChainResult execute(Map<String, String> variables) {
//        var tasks = this.promptChains.stream()
//            .map(p -> (Callable<Object>) () -> p.execute(variables)).toList();
//
//        List<Object> results = tasks.parallelStream()
//            .map(task -> {
//                try {
//                    return task.call();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            })
//            .toList();
//
//        Map<Class, Object> response = new HashMap<>();
//        results.forEach(r -> response.put(r.getClass(), r));
//        return new ParallelPromptChainResult(response);
//    }
}
