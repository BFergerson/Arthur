package com.codebrig.omnisrc.observe.observations

import java.util.concurrent.atomic.AtomicInteger

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ObservedRoles {

    public Map<String, AtomicInteger> observations = new HashMap<>()

    void observe(Iterator<String> roles) {
        def roleList = roles.toList()
        roleList.each {
            observations.putIfAbsent(it, new AtomicInteger())
            observations.get(it).incrementAndGet()
        }

        if (roleList.size() > 1) {
            //add merged super role
            def sb = new StringBuilder()
            def alphaSortRoles = new ArrayList<String>(roleList)
            alphaSortRoles.sort(String.CASE_INSENSITIVE_ORDER)
            for (int i = 0; i < alphaSortRoles.size(); i++) {
                sb.append(alphaSortRoles.get(i))
                if ((i + 1) < alphaSortRoles.size()) {
                    sb.append("_")
                }
            }
            def superRole = sb.toString()
            observations.putIfAbsent(superRole, new AtomicInteger())
            observations.get(superRole).incrementAndGet()
        }
    }

    void removeObservation(String role) {
        observations.remove(role)
    }

    List<String> getRoles() {
        return observations.keySet().toList()
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
