package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class GameRoomsLoader {

    private static final String ROOMS_DIR = "rooms";

    // Varre a pasta rooms e cria as instâncias correspondentes
    public Map<String, Room> loadAllRooms(GameEngine engine) throws Exception {
        Map<String, Room> loadedRooms = new HashMap<>();
        File dir = new File(ROOMS_DIR);

        // 1. Se a pasta não existe, lança erro logo (Básico)
        if (!dir.exists()) {
            throw new FileNotFoundException("A pasta '" + ROOMS_DIR + "' não existe!");
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                // Filtra para garantir que só tenta ler .txt (evita lixo do sistema)
                if (f.getName().endsWith(".txt")) {
                    Room r = Room.readRoom(f, engine);
                    if (r != null) {
                        loadedRooms.put(f.getName(), r);
                    }
                }
            }
        }
        
        // 2. Se chegou ao fim e não carregou nada, o jogo não pode começar (Essencial)
        if (loadedRooms.isEmpty()) {
            throw new IllegalStateException("A pasta existe, mas não foram encontrados níveis válidos (.txt).");
        }

        System.out.println("Salas carregadas: " + loadedRooms.size());
        return loadedRooms;
    }
}