package br.com.cinq.androidskilltest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.cinq.androidskilltest.persistencia.Usuario;

public class SessaoSharedPreferences {

    public static final String USUARIO_LOGADO = "USUARIO_LOGADO";
    public static final String ID_USUARIO_LOGADO = "ID_USUARIO_LOGADO";
    public static final String NOME_USUARIO_LOGADO = "NOME_USUARIO_LOGADO";
    public static final String EMAIL_USUARIO_LOGADO = "EMAIL_USUARIO_LOGADO";

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getIDUsuarioLogado(Context context) {
        return getPreferences(context).getInt(ID_USUARIO_LOGADO, 0);
    }

    public static void setUsuarioLogado(Context context, Usuario usuario) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        setUsuarioLogado(editor, true);
        setIDUsuarioLogado(editor, usuario.getId());

        setNomeUsuarioLogado(editor, usuario.getNome());
        setEmailUsuarioLogado(editor, usuario.getEmail());
        editor.apply();
    }


    public static void setDeslogado(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        setUsuarioLogado(editor, false);
        setIDUsuarioLogado(editor, -1);
        setNomeUsuarioLogado(editor, "");
        setEmailUsuarioLogado(editor, "");

        editor.apply();
    }

    public static boolean isUsuarioLogado(Context context) {
        return getPreferences(context).getBoolean(USUARIO_LOGADO, false);
    }

    private static void setUsuarioLogado(SharedPreferences.Editor editor, boolean valor) {
        editor.putBoolean(USUARIO_LOGADO, valor);
    }

    private static void setIDUsuarioLogado(SharedPreferences.Editor editor, int valor) {
        editor.putInt(ID_USUARIO_LOGADO, valor);
    }

    private static void setNomeUsuarioLogado(SharedPreferences.Editor editor, String valor) {
        editor.putString(NOME_USUARIO_LOGADO, valor);
    }

    private static void setEmailUsuarioLogado(SharedPreferences.Editor editor, String valor) {
        editor.putString(EMAIL_USUARIO_LOGADO, valor);
    }


}

