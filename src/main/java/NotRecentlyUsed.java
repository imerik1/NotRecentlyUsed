import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class NotRecentlyUsed {

    // 1- Apenas de leitura
    // 2- Todos que iniciam começam como 1 0
    // 3- Cada vez que entra um processo novo, o clock zera o R para 0
    // 4- Caso o processo seja referenciado de novo, ele fica com R para 1

    private static List<Processo> ram = new ArrayList<>();
    private static Integer pageFault = 0;
    private static final Integer MAX = 11;

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        int n = 0;
        while (n < MAX) {
            // Aparecer após a primeira transição
            if (ram.size() > 0) {
                System.out.println(" \n" + "=== P === R === M === T\n");
                for (Processo processo : ram) {
                    System.out.println("=== " + processo.getID() + " === " + processo.getR() + " === " + processo.getM()
                            + " === " + processo.getC() + "s");
                }
                System.out.println(" ");
                // Apenas para aparecer mais uma vez, o MAX na realidade é 10
                if (n == 10) {
                    break;
                }
            }
            System.out.print("Digite o nome(número ou letra) do processo(Apenas processos de LEITURA)\nProcesso: ");
            String opcao = entrada.next();
            if (ram == null || ram.size() < 3) {
                // Cria os três primeiros números
                createMemory(opcao);
            } else {
                // Gerencia as outras partes
                managementMemory(opcao);
            }
            n++;
        }
        entrada.close();
        // Após tudo, gera um relatório
        System.out.println(" \n" + "Page Fault = " + pageFault);
    }

    // Imprimi qual classe faz parte
    public static int getType(Processo processo) {
        if (processo.getR() == 0 && processo.getM() == 0) {
            return 0;
        } else if (processo.getR() == 0 && processo.getM() == 1) {
            return 1;
        } else if (processo.getR() == 1 && processo.getM() == 0) {
            return 2;
        } else {
            return 3;
        }
    }

    public static void createMemory(String opcao) {
        // Casos dos 3 primeiros n
        // 1- O número pode existir na memória, zerar ele e referenciar
        // 2- Incrementar todos existentes se não existir na memória o processo
        // 3- A referencia torna 0 se adicionar algum processo a memória
        boolean exist = Boolean.FALSE;
        if (ram != null) {
            for (Processo processo : ram) {
                // Se existir 1 processo existente, ele fica referenciado e zera seu ciclo de
                // vida
                if (processo.getID().equals(opcao)) {
                    processo.setR(1);
                    processo.setC(0);
                    exist = Boolean.TRUE;
                    break;
                }
            }
        }
        // Por que existe isso? Não poderia fazer um else acima por conta da incerteza
        // Não sabemos se realmente existe um processo igual aquele
        if (exist) {
            for (Processo processo : ram) {
                if (!processo.getID().equals(opcao)) {
                    processo.setC(processo.getC() + 1);
                }
            }
        }

        // Caso não exista um número, popula os processos na memória
        if (!exist) {
            // Para cada processo já existente, incrementa no seu ciclo
            for (Processo processo : ram) {
                processo.setR(0);
                processo.setC(processo.getC() + 1);
            }
            // Adiciona o novo processo a memória
            ram.add(new Processo(opcao, 1, 0, 0));
            pageFault++;
        }
    }

    public static void managementMemory(String opcao) {
        // Caso de gerenciamento
        // Verificar se já existe na memória
        // A menor classe vai embora
        // Caso existe mais de um processo com a mesma classe, usa o ciclo de vida dela
        // para desempatar

        boolean exist = Boolean.FALSE;
        // Processo caso existe na memória
        // Mesmo processo da função acima
        for (Processo processo : ram) {
            if (processo.getID().equals(opcao)) {
                exist = Boolean.TRUE;
                processo.setR(1);
                processo.setC(0);
                break;
            }
        }

        // Mesmo processo da função acima
        if (exist) {
            for (Processo processo : ram) {
                if (!processo.getID().equals(opcao)) {
                    processo.setC(processo.getC() + 1);
                }
            }
        }

        // caso não exista...
        if (!exist) {
            int menorClasse = 0;
            Processo vaiSair = null;
            for (Processo processo : ram) {
                // Iniciamos o primeiro processo como comparador
                if (vaiSair == null) {
                    vaiSair = processo;
                } else {
                    // Caso a classe do processo atual seja maior do qual está alocado, adicionamos
                    // ele
                    if (getType(processo) < menorClasse) {
                        vaiSair = processo;
                    }
                    // Caso o processo atual tenha a mesma classe porém o
                    // ciclo dela seja maior, ela será alocada
                    if (getType(vaiSair) == getType(processo) && processo.getC() > vaiSair.getC()) {
                        vaiSair = processo;
                    }
                }
                menorClasse = getType(vaiSair);
            }
            int i = 0;
            for (Processo processo : ram) {
                if (vaiSair.getID().equals(processo.getID())) {
                    ram.set(i, new Processo(opcao, 1, 0, 0));
                    pageFault++;
                    continue;
                } else {
                    processo.setR(0);
                    processo.setC(processo.getC() + 1);
                }
                i++;
            }
        }
    }
}