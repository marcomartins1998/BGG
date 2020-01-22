# BGG
Este projeto consiste na criação de uma app Android que habilita o utilizador a fazer várias pesquisas e ações sobre jogos de tabuleiros adquiridos através da API da Board Game Atlas.
De seguida tento explicar a função de vários módulos que compõem este projeto que achei importantes para o entendimento do funcionamento da aplicação.

## MainActivity:

<p align="center">
  <img src="https://github.com/marcomartins1998/BGG/blob/master/screenshots/MainActivity.png"/>
</p>

Esta _activity_ contém o _setup_ de todos os botões associados às pesquisas de jogos e de _features_ especiais solicitas no enunciado, sendo estas:
* Apresentação da lista de jogos mais populares;
* Apresentação da lista de jogos que foram publicados por uma dada empresa;
* Apresentação da lista de jogos que foram criados por um dado indivíduo;
* Apresentação da lista de jogos por nome;
* Gestão de listas de jogos personalizadas;	
* Gestão de favoritos.

Cada um dos primeiros 3 botões tem a ele associado um setOnClickListener que, quando ativado, cria uma _GameListActivity_ própria para a visualização de uma lista de jogos, passando-lhe o _URL_ de pesquisa, adquirido através da junção de informação na caixa de texto de _input_ e da _string_ única a cada botão, pelo seu _intent_ de forma a que a _activity_ possa fazer um pedido _GET_ pelos jogos que pretende demonstrar. O quarto botão de pesquisa pelos jogos mais populares funciona de forma semelhante aos anteriores com exceção de não depender do conteúdo da caixa de _input_ já que este faz sempre o mesmo tipo de pedido. Os botões de gestão de listas de jogos personalizadas e de gestão de favoritos redirecionam o utilizador para as _activities_ _ListManagingActivity_ e _FavouriteActivity_ respetivamente.

## GameListActivity:

<p align="center">
  <img src="https://github.com/marcomartins1998/BGG/blob/master/screenshots/GameListActivity.png"/>
</p>
 
Esta _activity_ está encarregada de fazer um pedido _GET_ a um _URL_ que lhe foi passado pelo _intent_ e demonstrar os jogos que vêem na resposta no seu _RecyclerView_, utilizamos um _ViewModel_ (_GamesViewModel_) para evitar pedidos _HTTP_ consecutivos à _API_ quando o utilizador roda o ecrã, foi também utilizada a tecnologia _LiveData_ dentro do _ViewModel_ para que, caso o utilizador entre na _activity_ e rode o ecrã, criando uma nova _activity_, antes que o pedido esteja concluído, a nova _activity_ possa ser notificada assim que o pedido iniciado na _activity_ anterior concluir através do método _observe()_, evitando mais um pedido desnecessário à _API_.
 
Caso a aplicação seja fechada pelo sistema e não pelo utilizador esta _activity_ vai guardar o _ViewModel_ para que quando a aplicação for aberta pelo utilizador esta possa reconstruir o estado da _activity_, passando o _Bundle_ _savedInstanceState_ a um _ViewModelProviderFactory_ (_GamesViewModelProviderFactory_) e evitando um acesso ao _intent_ da _activity_ desnecessário. Quando o pedido for concluído o _adapter_ associado ao _RecyclerView_ desta _activity_ acede ao _ViewModel_ e mostra cada jogo com uma imagem, nome e rating. Cada caixa associada aos jogos disponibiliza dois botões, um de adicionar o jogo a uma lista personalizada que cria uma _AddToCustomListActivity_, onde o utilizador pode escolher a qual das listas personalizadas existentes adicionar o jogo, e um para ver os detalhes do jogo através da criação de uma _GameDetailsActivity_.

Também foi implementado paginação à lista, através da manipulação do _URL_ com as queries _limit_ e _skip_ mostra-se 30 elementos de cada vez, alterando o valor de _skip_ e fazendo um novo pedido cada vez que o utilizador pressiona o botão _previous_, diminuindo o valor de _skip_, caso este seja maior ou igual a 30, por 30, ou _next_, aumentando o valor de _skip_ por 30.

## GameDetailsActivity:

<p align="center">
  <img src="https://github.com/marcomartins1998/BGG/blob/master/screenshots/GameDetailsActivity1.png"/>
  <img src="https://github.com/marcomartins1998/BGG/blob/master/screenshots/GameDetailsActivity2.png"/>
</p>

Mostra informação mais detalhada de um jogo. Foi utilizado um _ViewModel_ (_GameViewModel_) e _ViewModelProviderFactory_ (_GameViewModelProviderFactory_) pela mesma razão que foram usados no _GameListActivity_.

O utilizador pode pressionar a imagem para ser redirecionado para uma página _WEB_ que representa o jogo, pode também pressionar o link das regras para ser redirecionado para uma página _WEB_ que explica as regras. Ambos o _Primary publisher_ e os _Designers_ têm um botão a sí associados para facilitar a procura de jogos pelos respetivos, ao pressionar é criada uma _GameListActivity_ e o utilizador é redirecionado para a mesma.

## ListManagingActivity:

<p align="center">
  <img src="https://github.com/marcomartins1998/BGG/blob/master/screenshots/ListManagingActivity.png"/>
</p>

Esta está encarregue da criação e listagem de listas personalizadas de jogos criadas pelo utilizador. Quando o botão de criação de lista é premido este executa uma _AsyncTask_ que por sí vai correr numa _background thread_ uma _query_ de _SQL_ de inserção de _CustomList_. A ação de inserção é impedida caso a caixa de _input_ estiver vazia ou já exista uma _CustomList_ com o nome introduzido.

Esta _activity_ utiliza uma _ViewModel_ em junção com _LiveData_ para poder fazer um pedido à base de dados através de uma _query_ de _SQL_ para aceder a todas as _CustomLists_ existentes.  
Cada elemento da lista tem a sí associado um botão _view_ e _remove_. O botão de _view_ cria uma _CustomListActivity_ onde todos os jogos associados à lista são adquiridos através de um acesso à base de dados e mostrados numa lista cujo formato é igual ao da _activity_ _GameListActivity_, a única diferença sendo que cada jogo já não tem a sí associado um botão de adição a listas personalizadas. O botão _remove_ remove a lista da base de dados.

## FavouriteActivity

<p align="center">
  <img src="https://github.com/marcomartins1998/BGG/blob/master/screenshots/FavouriteActivity.png"/>
</p>
 
Esta _activity_ disponibiliza um espaço de criação de listas de favoritos, listas formadas por filtros definidos pelo utilizador, com a inserção do nome da lista e os vários filtros que a compõem e uma listagem de todas as listas de favoritos. Utiliza um _ViewModel_ em junção com _LiveData_ para poder fazer um pedido à base de dados, através de uma _query_ de _SQL_, para aceder a todos os _Favourites_ existentes, cada um deles tem a sí associado um botão de remoção e um botão de pesquisa, ao premir este botão é criada uma _ListFavouriteGamesActivity_ onde são listados todos os jogos que cumprem os filtros da lista de favoritos.

Existe também um botão que, quando pressionado, cria uma _ModifyUpdatesActivity_ onde o utilizador pode alterar as condições em que os _updates_ são feitos.

## ListFavouriteGamesActivity

<p align="center">
  <img src="https://github.com/marcomartins1998/BGG/blob/master/screenshots/ListFavouriteGamesActivity.png"/>
</p>
 
Tem o objetivo de demonstrar todos os jogos com os filtros associados ao _Favourite_, caso este não tenha jogos associados a sí na base de dados, um pedido _GET_ é feito para receber todos os jogos que se adaptam aos filtros, de seguida estes são armazenados na base de dados. Utiliza um _ViewModel_ em junção com _LiveData_ para que a lista visualizada possa ser preenchida com os jogos resultantes do pedido à base de dados ou do pedido _HTTP_ assim que estes estejam concluídos.

Uma classe que é usada nesta _activity_ e à qual temos que dar ênfase é a _FavouriteRepository_. O método _find
ByName()_ consiste na criação de um _MutableLiveData<List<Game>>_, preenchendo o _value_ deste com a lista de jogos resultantes do pedido à base de dados, que é feito através do uso da _FavouriteGamesAsyncTask_, de seguida o método _checkFavouriteGame()_ é chamado, este verifica que a lista de jogos não está vazia, caso esteja é efetuado um pedido _GET_ para receber todos os jogos associados a estes filtros, se este for efetuado com sucesso os jogos são armazenados na base de dados e associados ao _Favourite_.

## BGGApp

A classe que extende _Application_ no nosso projeto, é aqui que são instanciadas as classes que habilitam acceso à _WEB API_ da _Board Games Atlas_ para depois serem utilizadas por _activities_ ao longo da execução da nossa aplicação.

Também é aqui que a aplicação inicia um _NotificationChannel_, onde as notificações geradas pela aplicação iram surgir, e um _WorkRequest_, através do _WorkManager_, cujo o _WorkerFavouriteGames_ a ele associado está encarregue de realizar um pedido à _API_ por cada _Favourite_ armazenado na base de dados para verificar se houve alguma atualização à lista de jogos correspondente, caso haja uma notificação é lançada para alertar o utilizador das alterações que ocorreram.

## ModifyUpdatesActivity

<p align="center">
  <img src="https://github.com/marcomartins1998/BGG/blob/master/screenshots/ModifyUpdatesActivity.png"/>
</p>
 
Uma _activity_ cujo objetivo é alterar as circunstâncias em que a aplicação pode verificar se ocorreram atualizações aos seu _Favourites_. Caso o utilizador imponha novas condições a _WorkRequest_ antiga é eliminada e substituída por uma _WorkRequest_ que impõe essas mesmas condições.
