package com.autodoc.ai.shared.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class EventFileProcessingService implements EventFileProcessingProducer, EventFileProcessingConsumer {
    private final Map<Long, List<Path>> totalEventsMap;
    private final Map<Long, Map<String,Integer>> processedEventsMap;
//    private final ReentrantLock lock;

    public List<Path> getPaths(Long appId) {
        return new ArrayList<>(this.totalEventsMap.get(appId));
    }

    public EventFileProcessingService() {
        this.totalEventsMap = new ConcurrentHashMap<>();
        this.processedEventsMap = new ConcurrentHashMap<>();
//        this.lock = new ReentrantLock();
    }

    public void setFilesPathEvent(Long appId, List<Path> paths) {
        totalEventsMap.put(appId, paths);
    }

    public void incrementProcessedFiles(Long appId, String serviceName) {
        var serviceProcessByApp = processedEventsMap.computeIfAbsent(appId, k -> new ConcurrentHashMap<>());
        serviceProcessByApp.merge(serviceName, 1, Integer::sum);
    }

    public boolean consumeFilesProcessFinished(Long appId, String serviceName) {
        if(this.processedEventsMap.get(appId) == null) {
            return false;
        }
        if(this.processedEventsMap.get(appId).get(serviceName) == null) {
            return false;
        }

        var isProcessFinished = Objects.equals(this.totalEventsMap.get(appId).size(), this.processedEventsMap.get(appId).get(serviceName));
        if(isProcessFinished){

            totalEventsMap.remove(appId);
            processedEventsMap.get(appId).remove(serviceName);
            if(processedEventsMap.get(appId).isEmpty()){
                processedEventsMap.remove(appId);
            }
            return true;
        }
        return false;

    }

}
