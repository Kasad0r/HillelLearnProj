package org.kasad0r;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.kasad0r.entity.Skill;
import org.kasad0r.entity.Tool;
import org.kasad0r.entity.User;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.kasad0r.BasicJDBCDemo.connection;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicJDBCDemoTest {

    @Mock
    private ResultSet rs;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private Connection conn;

    private User user;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(connection()).thenReturn(conn);

        user = new User();
        user.setName("Anton");
        user.setId(1);
        user.setSkills(Collections.singletonList(new Skill().setId(2).setName("Soft")));
        user.setTools(Collections.singletonList(new Tool().setId(3).setName("Java Core")));
        when(rs.first()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn(user.getName());

    }
}