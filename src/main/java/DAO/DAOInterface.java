package DAO;
import java.util.List;

public interface DAOInterface<T> {

    List<T> getAll();

    T getByID(int ID);

    T insert (T obj);

    //boolean update (T obj);

    //boolean delete (T obj);

}
