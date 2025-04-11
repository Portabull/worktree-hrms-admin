package com.worktree.hrms.dao.impl;

import com.worktree.hrms.dao.DBStatsDao;
import com.worktree.hrms.utils.HibernateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DBStatsDaoImpl implements DBStatsDao {

    @Autowired
    private HibernateUtils hibernateUtils;

    @Autowired
    private EntityManager entityManager;


    @Override
    public Map<String, Object> getDBStats(String type, Optional<String> schemaName, Optional<String> tableName) {
        Map<String, Object> response = new HashMap<>();
        try (Session session = hibernateUtils.getSession()) {
            if (type.equalsIgnoreCase("schema")) {
                String totalDBStorage = (String) session.createNativeQuery("SELECT pg_size_pretty(pg_database_size(current_database())) AS db_size").uniqueResult();
                response.put("totalDBStorage", totalDBStorage);

                List<Map<String, Object>> chartData = new ArrayList<>();

                List<Object[]> schemaStats = session.createNativeQuery("SELECT schemaname,COUNT(*) AS table_count,pg_size_pretty(SUM(pg_total_relation_size(relid))) AS total_size FROM pg_catalog.pg_statio_user_tables GROUP BY schemaname ORDER BY SUM(pg_total_relation_size(relid)) DESC").list();

                List<Object[]> data = new ArrayList<>();

                Object[] columnNames = {"Schema Name", "Table Count", "Schema Size"};

                data.add(columnNames);

                for (Object[] obj : schemaStats) {
                    chartData.add(Map.of("name", obj[0], "count", obj[1], "size", obj[2]));
                    data.add(obj);
                }

                response.put("tableData", data);
                response.put("chartData", chartData);
            } else if (type.equalsIgnoreCase("retrieveSchema") && schemaName.isPresent()) {
                String query = "SELECT  table_name, CASE WHEN pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) >= 1073741824 THEN ROUND(pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) / 1073741824.0, 2) || ' GB' WHEN pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) >= 1048576 THEN ROUND(pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) / 1048576.0, 2) || ' MB' WHEN pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) >= 1024 THEN ROUND(pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) / 1024.0, 2) || ' KB' ELSE '0 KB' END AS total_size FROM information_schema.tables WHERE table_type = 'BASE TABLE' AND table_schema = '" + schemaName.get() + "' ORDER BY pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) DESC";
                List<Object[]> schemaStats = session.createNativeQuery(query).list();


                List<Object[]> data = new ArrayList<>();

                Object[] columnNames = {"Table Name", "Table Size"};

                data.add(columnNames);

                for (Object[] obj : schemaStats) {
                    data.add(obj);
                }

                response.put("tableData", data);
            } else if (type.equalsIgnoreCase("retrieveTable") && schemaName.isPresent() && tableName.isPresent()) {
                String query = "SELECT * from " + schemaName.get() + "." + tableName.get() + " limit 100";

                List<Object[]> data = new ArrayList<>();

                List<Tuple> results = entityManager.createNativeQuery(query, Tuple.class).getResultList();

                List<String> columnNames = results.get(0).getElements().stream()
                        .map(TupleElement::getAlias)
                        .collect(Collectors.toList());

                data.add(columnNames.toArray());

                for (Tuple tuple : results) {
                    Object[] row = new Object[columnNames.size()];
                    for (int i = 0; i < columnNames.size(); i++) {
                        row[i] = tuple.get(columnNames.get(i));
                    }
                    data.add(row);
                }

                response.put("tableData", data);
            } else if (type.equalsIgnoreCase("currentConnections")) {
                String query = "SELECT datname AS database_name, usename AS user_name, client_addr, application_name, state, backend_start, query_start, wait_event_type, wait_event, query FROM pg_stat_activity WHERE state = 'active';";

                List<Object[]> data = new ArrayList<>();

                List<Tuple> results = entityManager.createNativeQuery(query, Tuple.class).getResultList();

                if (!CollectionUtils.isEmpty(results)) {
                    List<String> columnNames = results.get(0).getElements().stream()
                            .map(TupleElement::getAlias)
                            .collect(Collectors.toList());

                    data.add(columnNames.toArray());

                    for (Tuple tuple : results) {
                        Object[] row = new Object[columnNames.size()];
                        for (int i = 0; i < columnNames.size(); i++) {
                            row[i] = tuple.get(columnNames.get(i));
                        }
                        data.add(row);
                    }
                }

                response.put("tableData", data);
            } else if (type.equalsIgnoreCase("longRunningQueries")) {
                String query = "SELECT pid, datname, usename, state, query, now() - query_start AS duration, query_start FROM pg_stat_activity WHERE state = 'active' AND now() - query_start > interval '30 seconds' ORDER BY duration DESC;";

                List<Object[]> data = new ArrayList<>();

                List<Tuple> results = entityManager.createNativeQuery(query, Tuple.class).getResultList();

                if (!CollectionUtils.isEmpty(results)) {
                    List<String> columnNames = results.get(0).getElements().stream()
                            .map(TupleElement::getAlias)
                            .collect(Collectors.toList());

                    data.add(columnNames.toArray());

                    for (Tuple tuple : results) {
                        Object[] row = new Object[columnNames.size()];
                        for (int i = 0; i < columnNames.size(); i++) {
                            row[i] = tuple.get(columnNames.get(i));
                        }
                        data.add(row);
                    }
                }
                response.put("tableData", data);
            }


        }
        return response;
    }


}
