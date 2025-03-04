package DAO;
import java.util.List;

public interface DAOInterface<T> {

    void getByID();

    List<T> getAll();

    T insert (T obj);

    boolean update (T obj);

    boolean delete (T obj);

}
