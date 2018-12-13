package br.com.cinq.androidskilltest.persistencia;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Single;

@Dao
public interface UsuarioDao {


    @Insert
    void inserir(Usuario usuario);

    @Update
    void atualizar(Usuario usuario);

    @Query("SELECT * FROM usuario WHERE email = :email ")
    Single<Usuario> buscarPorEmail(String email);

    @Query("SELECT * FROM usuario WHERE id = :id")
    Single<Usuario> buscarPorID(int id);

    @Query("DELETE FROM usuario WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM usuario ")
    Single<List<Usuario>> buscarTodos();


    @Query("SELECT count(*) FROM usuario WHERE id != :idIgnorar AND email = :email ")
    Single<Integer> countEmailUtilizado(String email, int idIgnorar);
}
