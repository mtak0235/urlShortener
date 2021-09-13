package seoul.urlShortener.repository;

import seoul.urlShortener.domain.Url;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class UrlRepositoryImpl implements UrlRepository {

    private final DataSource dataSource;

    public UrlRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Url save(Url url) throws Exception {
        String sql = "insert into url(longurl) values(?)";
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, url.getLongurl());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                url.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return url;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    @Override
    public Optional<Url> findById(Long id) {
        String sql = "select * from url where id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Url url = new Url();
                url.setId(rs.getLong("id"));
                url.setLongurl(rs.getString("longurl"));
                return Optional.of(url);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    @Override
    public Optional<Url> findByLongurl(String longUrl) {
        String sql = "select * from url where longurl = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            pstmt = connection.prepareStatement(sql);
           pstmt.setString(1, longUrl);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Url url = new Url();
                url.setId(rs.getLong("id"));
                url.setLongurl(rs.getString("longurl"));
                return Optional.of(url);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, pstmt, rs);
        }
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }
}
