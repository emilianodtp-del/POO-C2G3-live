package pe.edu.upeu.sysventas.service;

import java.util.List; // ✅ Import necesario
import pe.edu.upeu.sysventas.dto.ComboBoxOption; // ✅ Import necesario
import pe.edu.upeu.sysventas.model.Perfil;

public interface IPerfilService extends ICrudGenericoService<Perfil, Long> {

    List<ComboBoxOption> listarCombobox();

}
