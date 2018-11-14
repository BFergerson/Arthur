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

    final Map<String, AtomicInteger> individualObservations = new HashMap<>()
    final Map<String, AtomicInteger> actualObservations = new HashMap<>()

    void observe(Iterator<String> roles, boolean observeActual) {
        def roleList = roles.toList()
        roleList.each {
            individualObservations.putIfAbsent(it, new AtomicInteger())
            individualObservations.get(it).incrementAndGet()
        }

        if (roleList.size() > 1) {
            if (observeActual) {
                //add actual role
                def sb = new StringBuilder()
                def alphaSortRoles = new ArrayList<String>(roleList)
                alphaSortRoles.sort(String.CASE_INSENSITIVE_ORDER)
                for (int i = 0; i < alphaSortRoles.size(); i++) {
                    sb.append(alphaSortRoles.get(i))
                    if ((i + 1) < alphaSortRoles.size()) {
                        sb.append("_")
                    }
                }

                def actualRole = sb.toString()
                actualObservations.putIfAbsent(actualRole, new AtomicInteger())
                actualObservations.get(actualRole).incrementAndGet()
            }
        }
    }

    void removeObservation(String role) {
        individualObservations.remove(role)
    }

    List<String> getPossibleSemanticRoles() {
        def rtnList = new ArrayList<String>()
        def individualRoles = individualSemanticRoles
        individualRoles.sort(String.CASE_INSENSITIVE_ORDER)
        for (int i = 0; i < individualRoles.size(); i++) {
            String s = ""
            for (int j = i; j < individualRoles.size(); j++) {
                if (!s.isEmpty()) {
                    s += "_"
                }
                s += individualRoles.get(j)
                if (!individualObservations.containsKey(s) && !actualObservations.containsKey(s)) {
                    rtnList.add(s)
                }
            }
        }
        return rtnList
    }

    List<String> getIndividualSemanticRoles() {
        return individualObservations.keySet().toList()
    }

    List<String> getActualSemanticRoles() {
        return actualObservations.keySet().toList()
    }

    List<String> getRoles(boolean includePossibleRoles) {
        def rtnList = individualSemanticRoles + actualSemanticRoles
        if (includePossibleRoles) {
            return rtnList + possibleSemanticRoles
        }
        return rtnList
    }

    List<String> getRankedRoles(boolean includePossibleRoles) {
        def rtnList = entriesSortedByValues(individualObservations + actualObservations).keySet().toList()
        if (includePossibleRoles) {
            return rtnList + possibleSemanticRoles
        }
        return rtnList
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
