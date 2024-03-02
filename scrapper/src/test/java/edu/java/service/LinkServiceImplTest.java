package edu.java.service;

class LinkServiceImplTest {
//    LinkServiceImpl linkService;
//
//    @BeforeEach
//    void setUp() {
//        linkService = new LinkServiceImpl();
//    }
//
//    @Test
//    void getByNameAndChatId() throws URISyntaxException {
//        //given
//        Link expected = new Link(new URI("test"));
//        linkService.registerChatId(100);
//        linkService.track("test", 100);
//        //when
//        Optional<Link> actual = linkService.getByNameAndChatId("test", 100);
//        //then
//        Assertions.assertEquals(actual.get(), expected);
//    }
//
//    @Test
//    @DisplayName("Should throw if no chatId in db")
//    void trackNoChatId() {
//        Assertions.assertThrows(InvalidChatIdException.class, () -> linkService.track("test", 1));
//    }
//
//    @Test
//    @DisplayName("Should throw if duplicate link")
//    void trackDuplicateLink() {
//        linkService.registerChatId(100);
//        linkService.track("test", 100);
//        Assertions.assertThrows(DuplicateLinkException.class, () -> linkService.track("test", 100));
//    }
//
//    @Test
//    @DisplayName("Should throw if bad Uri")
//    void trackBadUri() {
//        linkService.registerChatId(100);
//        Assertions.assertThrows(URIException.class, () -> linkService.track("test dsfds sdfdwsew", 100));
//    }
//
//    @Test
//    void registerChatId() {
//        linkService.registerChatId(100);
//        Assertions.assertTrue(linkService.getInMemoryDb().containsKey(100L));
//    }
//    @Test
//    @DisplayName("Should throw if chat already registered")
//    void registerChatIdDuplicate() {
//        linkService.registerChatId(100);
//        Assertions.assertThrows(InvalidChatIdException.class, () -> linkService.registerChatId(100));
//    }
//
//    @Test
//    void untrack() {
//        linkService.registerChatId(100);
//        linkService.track("test", 100);
//
//        linkService.untrack("test", 100);
//
//        Assertions.assertEquals(0,linkService.getInMemoryDb().get(100L).size());
//    }
//    @Test
//    void untrackNoSuchLink() {
//        linkService.registerChatId(100);
//        linkService.track("test", 100);
//
//        Assertions.assertThrows(NoSuchLinkException.class, () -> linkService.untrack("test2", 100));
//    }
//    @Test
//    @DisplayName("Should throw if no chatId in db")
//    void untrackNoChatId() {
//        Assertions.assertThrows(InvalidChatIdException.class, () -> linkService.untrack("test", 1));
//    }
//
//    @Test
//    void getAllByChatId() throws URISyntaxException {
//        //given
//        linkService.registerChatId(100);
//        linkService.track("test", 100);
//        linkService.track("test2", 100);
//        List<Link> expected100=List.of(new Link(new URI("test")),new Link(new URI("test2")));
//        linkService.registerChatId(1);
//        linkService.track("test", 1);
//        List<Link> expected1=List.of(new Link(new URI("test")));
//        //when
//        List<Link> actual100=linkService.getAllByChatId(100);
//        List<Link> actual1=linkService.getAllByChatId(1);
//        //then
//        Assertions.assertEquals(expected100,actual100);
//        Assertions.assertEquals(expected1,actual1);
//    }
//    @Test
//    @DisplayName("Should throw if no chatId in db")
//    void getAllByChatIdNoChatId() {
//        Assertions.assertThrows(InvalidChatIdException.class, () -> linkService.getAllByChatId(1));
//    }
}
