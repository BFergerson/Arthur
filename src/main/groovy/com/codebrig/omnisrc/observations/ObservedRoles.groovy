package com.codebrig.omnisrc.observations

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author github.com/BFergerson
 */
class ObservedRoles {

    public Map<String, AtomicInteger> observations = new HashMap<>()

    void observe(Iterator<String> roles) {
        roles.each {
            observations.putIfAbsent(it, new AtomicInteger())
            observations.get(it).incrementAndGet()
        }
    }

    void removeObservation(String role) {
        observations.remove(role)
    }

    List<String> getRankedRoles() {
        return entriesSortedByValues(observations).keySet().toList()
    }

    private static Map<String, AtomicInteger> entriesSortedByValues(Map<String, AtomicInteger> map) {
        List<Map.Entry<String, AtomicInteger>> list = new LinkedList<Map.Entry<String, AtomicInteger>>(map.entrySet())
        Collections.sort(list, new Comparator<Map.Entry<String, AtomicInteger>>() {
            @Override
            int compare(Map.Entry<String, AtomicInteger> o1, Map.Entry<String, AtomicInteger> o2) {
                return o2.getValue().get() <=> o1.getValue().get()
            }
        })

        Map<String, AtomicInteger> result = new LinkedHashMap<String, AtomicInteger>()
        for (Map.Entry<String, AtomicInteger> entry : list) {
            result.put(entry.getKey(), entry.getValue())
        }
        return result
    }
}
