package br.com.cinq.androidskilltest.repositorio;

import android.app.Application;

import java.util.List;

import br.com.cinq.androidskilltest.persistencia.SkillTestRoomDatabase;
import br.com.cinq.androidskilltest.persistencia.Usuario;
import br.com.cinq.androidskilltest.persistencia.UsuarioDao;
import io.reactivex.Single;

public class UsuarioRepository {

    private UsuarioDao usuarioDao;

    public UsuarioRepository(Application application) {

        usuarioDao = SkillTestRoomDatabase.getDatabase(application).usuarioDao();

    }

    public Single<Integer> countEmailUtilizado(String email, int idIgnorar) {
        return usuarioDao.countEmailUtilizado(email, idIgnorar);
    }

    public Single<Usuario> getUsuario(String email) {

        return usuarioDao.buscarPorEmail(email);

    }

    public Single<Usuario> getUsuarioPorID(int id) {

        return usuarioDao.buscarPorID(id);

    }

    public Single<List<Usuario>> getTodosUsuarios() {

        return usuarioDao.buscarTodos();

    }

    public void inserirUsuario(Usuario usuario) {
        usuarioDao.inserir(usuario);
    }

    public void excluirUsuario(Usuario usuario) {
        usuarioDao.delete(usuario.getId());
    }

    public void atualizarUsuario(Usuario usuario) {
        usuarioDao.atualizar(usuario);
    }

}
