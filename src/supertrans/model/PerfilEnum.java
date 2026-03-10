package supertrans.model;

/**
 * Enumeração que define os papéis corporativos permitidos para as empresas
 * cadastradas no portal Super Trans.
 * * A utilização deste Enum substitui a digitação livre de Strings, garantindo
 * a integridade (padronização) dos dados antes de serem persistidos no PostgreSQL.
 */
public enum PerfilEnum {
    FORNECEDOR,
    REVENDEDOR,
    CLIENTE_FINAL;
}