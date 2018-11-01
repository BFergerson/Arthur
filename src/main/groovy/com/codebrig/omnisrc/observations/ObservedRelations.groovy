package com.codebrig.omnisrc.observations

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author github.com/BFergerson
 */
class ObservedRelations {

    private Map<String, AtomicInteger> hasObservations = new HashMap<>()
    private Map<String, AtomicInteger> isObservations = new HashMap<>()

    void observeHas(String relation) {
        hasObservations.putIfAbsent(relation, new AtomicInteger())
        hasObservations.get(relation).incrementAndGet()
    }

    void observeIs(String relation) {
        isObservations.putIfAbsent(relation, new AtomicInteger())
        isObservations.get(relation).incrementAndGet()
    }

    List<String> getRankedHasRelations() {
        return entriesSortedByValues(hasObservations).keySet().toList()
    }

    List<String> getRankedIsRelations() {
        return entriesSortedByValues(isObservations).keySet().toList()
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
