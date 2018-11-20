package com.codebrig.omnisrc.observe.observations

import com.google.common.collect.Maps

import java.util.concurrent.atomic.AtomicInteger

/**
 * Structural relationships observed during source code parsing
 *
 * @version 0.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ObservedRelations {

    final Map<String, AtomicInteger> hasObservations = Maps.newConcurrentMap()
    final Map<String, AtomicInteger> isObservations = Maps.newConcurrentMap()

    void observeHas(String relation) {
        hasObservations.putIfAbsent(relation, new AtomicInteger())
        hasObservations.get(relation).incrementAndGet()
    }

    void observeIs(String relation) {
        isObservations.putIfAbsent(relation, new AtomicInteger())
        isObservations.get(relation).incrementAndGet()
    }

    List<String> getHasRelations() {
        return hasObservations.keySet().toList()
    }

    List<String> getRankedHasRelations() {
        return entriesSortedByValues(hasObservations).keySet().toList()
    }

    List<String> getIsRelations() {
        return isObservations.keySet().toList()
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
